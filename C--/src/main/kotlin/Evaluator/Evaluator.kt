package Evaluator

import Parser.ParserToken.*

class Evaluator {

    private val functionDeclarations = HashMap<String, Declaration.FunctionDeclare>()
    private val globalEnvironment = HashMap<String, Expression.Value>()

    fun eval( declarations: List<Declaration>, args : List<Expression.Value>? = null) : Expression.Value {

        declarations.forEach { d ->
            when(d){
                is Declaration.FunctionDeclare -> functionDeclarations[d.functionName] = d
                is Declaration.VariableDeclaration -> globalEnvironment[d.name] = evalExpression(d.expression, globalEnvironment)
                else -> throw Exception("Unknown Declaration: '$d'")
            }
        }
        val mainFunction = functionDeclarations["Main"] ?: throw Exception("Couldn't find Function 'Main'")

        return evalFunction(mainFunction,args)
    }

    private fun evalFunction(function : Declaration.FunctionDeclare, parameter: List<Expression.Value>?) : Expression.Value {

        if(function.parameters?.size != parameter?.size)
            throw Exception("function call '${function.functionName}(${function.parameters})' has to many or to little parameter")

        val localEnvironment = HashMap<String, Expression.Value>()
        function.parameters?.zip(parameter.orEmpty()){ fp, p -> fp.name to p }?.associateTo(localEnvironment){it.first to it.second} ?: HashMap<String, Expression.Value>()

        return evalBody(function.body, localEnvironment) ?: throw Exception("Couldn't find a return statement")
    }

    private fun evalProcedure(procedure : Declaration.FunctionDeclare, parameter: List<Expression.Value>?) {

        if(procedure.parameters?.size != parameter?.size)
            throw Exception("procedure call '${procedure.functionName}(${procedure.parameters})' has to many or to little parameter")

        val localEnvironment = HashMap<String, Expression.Value>()
        procedure.parameters?.zip(parameter.orEmpty()){ fp, p -> fp.name to p }?.associateTo(localEnvironment){it.first to it.second} ?: HashMap<String, Expression.Value>()

        evalBody(procedure.body, localEnvironment)
    }

    private fun evalBody(body: Body, environment: HashMap<String, Expression.Value>) : Expression.Value? {
        body.localVariables?.forEach { variable ->
            if(environment.containsKey(variable.name))
                throw Exception("Variable can't be initialized twice '${variable.name}'")

            environment[variable.name] = evalExpression(variable.expression, environment)
        }

        body.functionBody.forEach { statement ->
            when(statement){
                is Statement.AssignValue ->{
                    when(statement.variableName){
                        "return" -> return evalExpression(statement.expression, environment)
                        else -> {
                             when {
                                environment.containsKey(statement.variableName) -> environment[statement.variableName] = evalExpression(statement.expression, environment)
                                globalEnvironment.containsKey(statement.variableName) -> globalEnvironment[statement.variableName] = evalExpression(statement.expression, environment)
                                else -> throw Exception("Couldn't find variable '$statement.variableName}'")
                            }
                        }
                    }
                }
                is Statement.If -> {
                    val condition = evalExpression(statement.condition, environment).value as? ConstantValue.Boolean
                        ?: throw Exception("If condition must be of type 'bool'")
                    if(condition.value){
                        evalBody(statement.ifBody, environment)?.let { return it }
                    }else{
                        statement.elseBody?.let { evalBody(statement.elseBody, environment)?.let { return it } }
                    }
                }
                is Statement.While -> {
                    while ((evalExpression(statement.condition, environment).value as? ConstantValue.Boolean)?.value ?: throw Exception("While condition must be of type 'bool'"))
                    {
                        evalBody(statement.body, environment)?.let { return it }
                    }
                }
                is Statement.ProcedureCall ->{
                    when(statement.procedureName){
                        "Println" -> statement.parameterList?.map { evalExpression(it,environment) }?.forEach { p -> println(p.value.getValueAsString())}
                        "Print" -> statement.parameterList?.map { evalExpression(it,environment) }?.forEach { p -> print(p.value.getValueAsString())}
                        else -> {
                            val procedure = functionDeclarations[statement.procedureName] ?: throw Exception("Couldn't find procedure '${statement.procedureName}'")
                            evalProcedure(procedure, statement.parameterList?.map { evalExpression(it,environment) })
                        }
                    }
                }
                is Statement.Block -> throw NotImplementedError()

            }
        }
        return null
    }

    private fun evalExpression(expression: Expression, environment : HashMap<String, Expression.Value> ) : Expression.Value{
        return when(expression){
            is Expression.Value -> expression
            is Expression.UseVariable ->{
                environment.getOrDefault(expression.variableName,null) ?: globalEnvironment.getOrDefault(expression.variableName,null) ?: throw Exception("Variable couldn't be found ${expression.variableName}")
            }
            is Expression.Operation -> {
                if(expression.expressionB == null){
                    return when(expression.operator){
                        Operator.Not -> Expression.Value(ConstantValue.Boolean(!(evalExpression(expression.expressionA, environment).value as?  ConstantValue.Boolean ?: throw Exception("Booleans can only be negated")).value))
                        Operator.Minus-> Expression.Value(ConstantValue.Integer(-(evalExpression(expression.expressionA, environment).value as?  ConstantValue.Integer ?: throw Exception("Integer can only be negated")).value))
                        else -> throw Exception("Operation ${expression.operator} needs more then one Argument")
                    }
                    throw Exception("In this position operator: '=' isn't allowed")
                }
                else
                    return when(expression.operator){
                        Operator.DoubleEquals -> equalsValue(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x==y}
                        Operator.Plus -> addValue(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment))
                        Operator.Minus -> evalBinaryNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x-y}
                        Operator.Multiply -> evalBinaryNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x*y}
                        Operator.And -> evalBinaryBoolean(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x&&y}
                        Operator.Or -> evalBinaryBoolean(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x||y}

                        Operator.NotEqual -> equalsValue(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x!=y}
                        Operator.Less -> equalsValueNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x<y}
                        Operator.LessEqual -> equalsValueNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x<=y}
                        Operator.Greater -> equalsValueNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x>y}
                        Operator.GreaterEquals -> equalsValueNumber(evalExpression(expression.expressionA,environment),evalExpression(expression.expressionB,environment)){x,y -> x>=y}
                        Operator.Not -> throw Exception("to many Arguments. 'Not' only needs one")
                        Operator.Equals -> throw Exception("In this position operator: '=' isn't allowed")
                    }
            }
            is Expression.FunctionCall ->{
                return when(expression.functionName){
                    "ToString" -> toStringImplementation(expression.parameterList?.map { evalExpression(it,environment) } )
                    else -> {
                        val function = functionDeclarations.get(expression.functionName) ?: throw Exception("Couldn't find function '${expression.functionName}'")
                        return evalFunction(function, expression.parameterList?.map { evalExpression(it,environment) })
                    }
                }
            }
            else -> throw Exception("Unknown Expression '$expression'")
        }


    }

    private fun toStringImplementation(parameterList: List<Expression>?): Expression.Value{
        parameterList ?: throw Exception("Function ToString need one parameter")
        if(parameterList.size > 1)
            throw Exception("Function ToString only accepts one Parameter")

        val value = parameterList.first() as? Expression.Value ?: throw Exception("Can't ToString Expression: ${parameterList.first()}")
        return Expression.Value(ConstantValue.String(value.value.getValueAsString()))
    }

    private fun addValue(v1: Expression.Value, v2: Expression.Value): Expression.Value {
        return when(val v1n = v1.value){
            is ConstantValue.Integer -> {
                val v2n = v2.value as? ConstantValue.Integer ?: throw Exception("Can't add Type '${v2.value::class} to Integer'")
                Expression.Value(ConstantValue.Integer(v1n.value + v2n.value))
            }
            is ConstantValue.String -> {
                val v2n = v2.value as? ConstantValue.String ?: throw Exception("Can't add Type '${v2.value::class} to String'")
                Expression.Value(ConstantValue.String(v1n.value + v2n.value))
            }
            else -> throw Exception("Can't use add operation on [${v1.value} + ${v2.value}]")
        }
    }

    private fun evalBinaryNumber(v1: Expression.Value, v2: Expression.Value, f: (Int, Int) -> Int): Expression.Value {
        val v1n = v1.value as? ConstantValue.Integer ?: throw Exception("Can't use a binary operation on $v1, it's not a number")
        val v2n = v2.value as? ConstantValue.Integer ?: throw Exception("Can't use a binary operation on $v2, it's not a number")
        return Expression.Value( ConstantValue.Integer(f(v1n.value, v2n.value)))
    }

    private fun equalsValueNumber(v1: Expression.Value, v2: Expression.Value, f: (Int, Int) -> Boolean): Expression.Value {
        val v1n = v1.value as? ConstantValue.Integer ?: throw Exception("Can't use a binary operation on $v1, it's not a number")
        val v2n = v2.value as? ConstantValue.Integer ?: throw Exception("Can't use a binary operation on $v2, it's not a number")
        return  Expression.Value( ConstantValue.Boolean(f(v1n.value, v2n.value)))
    }

    private fun evalBinaryBoolean(v1: Expression.Value, v2: Expression.Value, f: (Boolean, Boolean) -> Boolean): Expression.Value {
        val v1n = v1.value as? ConstantValue.Boolean ?: throw Exception("Can't use a binary operation on $v1, it's not a boolean")
        val v2n = v2.value as? ConstantValue.Boolean ?: throw Exception("Can't use a binary operation on $v2, it's not a boolean")
        return Expression.Value( ConstantValue.Boolean(f(v1n.value, v2n.value)))
    }

    private fun <T> equalsValue(v1: T, v2: T, f: (T, T) -> Boolean): Expression.Value {
        return  Expression.Value( ConstantValue.Boolean(f(v1, v2)))
    }


}
