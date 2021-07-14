package TypeChecker

import Evaluator.Exceptions.NotFound.FunctionNotFoundRuntimeException
import Evaluator.Exceptions.NotFound.VariableNotFoundRuntimeException
import Parser.ParserToken.*
import TypeChecker.Exceptions.*

class TypeChecker(private val declarations: List<Declaration>, private val args : List<Expression.Value>? = null) {

    private val functionDeclarations = HashMap<String, Declaration.FunctionDeclare>()
    private val globalVariableDeclarations = HashMap<String, Declaration.VariableDeclaration>()

    fun check(){

        declarations.forEach { d ->
            when(d){
                is Declaration.FunctionDeclare -> functionDeclarations[d.functionName] = d
                is Declaration.VariableDeclaration -> {
                    checkVariableDeclaration(d, HashMap())
                    globalVariableDeclarations[d.name] = d
                }
            }
        }

        val mainFunction = functionDeclarations["Main"] ?: throw FunctionNotFoundRuntimeException("Main")

        checkParameter(mainFunction,args?.map { getExpressionType(it, HashMap())})
        checkFunctionDeclaration(mainFunction)

        functionDeclarations.forEach { f ->
            if(f.key != "Main")
                checkFunctionDeclaration(f.value)
        }

    }

    private fun checkFunctionDeclaration(functionDeclaration : Declaration.FunctionDeclare){
        checkBodyTypes(functionDeclaration.functionName ,functionDeclaration.body, functionDeclaration.returnType, functionDeclaration.parameters?.associate{it.name to it.type}?.let { HashMap(it)} ?: HashMap())
    }

    private fun checkParameter(functionDeclaration : Declaration.FunctionDeclare, args : List<Type>?){
        if(functionDeclaration.parameters?.size != args?.size)
            throw TypeCheckerFunctionParameterException(functionDeclaration.functionName,functionDeclaration.parameters,args)

        val parameterCombined = functionDeclaration.parameters?.zip(args.orEmpty()){ fp, p -> fp to p }

        parameterCombined?.forEach {
            if(it.first.type != it.second)
                throw TypeCheckerFunctionParameterException(functionDeclaration.functionName,it.first,it.second)
        }

    }


    private fun checkBodyTypes(functionName : String ,body: Body , returnType: Type? , upperVariables: HashMap<String, Type>?){


        val localVariables = body.localVariables?.let { HashMap(body.localVariables?.associate { it.name to it.type}) } ?: HashMap()

        val combinedVariables = combineVariables(upperVariables,localVariables)

        body.localVariables?.forEach { lv ->
            checkVariableDeclaration(lv, combinedVariables)
        }

        body.functionBody.forEach { statement ->
            when(statement){
                is Statement.AssignValue -> {
                    when(statement.variableName){
                        "return" -> {
                            val type = getExpressionType( statement.expression, combinedVariables)
                            if(returnType != type)
                                throw TypeCheckerReturnTypeException(functionName ,returnType, type)
                        }
                        else -> {
                            val type = getExpressionType( statement.expression, combinedVariables)
                            if (combinedVariables[statement.variableName] != type)
                                throw TypeCheckerWrongTypeAssignmentException(statement.variableName, combinedVariables[statement.variableName] ,type)
                        }
                    }
                }
                is Statement.Block -> {
                    checkBodyTypes(functionName, statement.body, returnType ,combinedVariables)
                }
                is Statement.If -> {
                    val conditionType = getExpressionType(statement.condition, combinedVariables)
                    conditionType as? Type.Boolean ?: throw TypeCheckerConditionException(conditionType)

                    checkBodyTypes(functionName, statement.ifBody, returnType, combinedVariables)
                    statement.elseBody?.let { checkBodyTypes(functionName, it, returnType, combinedVariables) }
                }
                is Statement.While -> {
                    val conditionType = getExpressionType(statement.condition, combinedVariables)
                    conditionType as? Type.Boolean ?: throw TypeCheckerConditionException(conditionType)

                    checkBodyTypes(functionName, statement.body, returnType, combinedVariables)
                }
                is Statement.ProcedureCall -> {
                    if(statement.procedureName != "Println")
                    {
                        val procedure = functionDeclarations[statement.procedureName] ?: throw TypeCheckerFunctionNotFoundException(statement.procedureName)
                        checkParameter(procedure, statement.parameterList?.map { getExpressionType(it, HashMap())})
                    }
                }
            }

        }

    }

    private fun combineVariables(upperEnvironment : HashMap<String, Type>?,lowerEnvironment : HashMap<String, Type>) : HashMap<String, Type>{
        if (upperEnvironment == null)
            return lowerEnvironment
        return HashMap((upperEnvironment.keys + lowerEnvironment.keys).associateWith { k -> lowerEnvironment[k] ?: upperEnvironment[k] ?: throw VariableNotFoundRuntimeException(k) })
    }

    private fun checkVariableDeclaration(variableDeclaration: Declaration.VariableDeclaration, localVariables : HashMap<String, Type>) {
        val type = getExpressionType(variableDeclaration.expression, localVariables)
        if(variableDeclaration.type != type)
            throw TypeCheckerWrongTypeAssignmentException(variableDeclaration.name, variableDeclaration.type, type)
    }

    private fun getExpressionType(expression: Expression, localVariables : HashMap<String, Type>): Type {
        return when(expression){
            is Expression.Value -> expression.value.getType()
            is Expression.FunctionCall -> {
                when(expression.functionName){
                    "ToString" -> {
                        if(expression.parameterList == null)
                            throw TypeCheckerFunctionParameterException("Function: 'ToString' must have one or more transfer parameters")
                        Type.String
                    }
                    else -> {
                        val function = functionDeclarations[expression.functionName] ?: throw TypeCheckerFunctionNotFoundException(expression.functionName)
                        checkParameter(function, expression.parameterList?.map { getExpressionType(it, localVariables)})

                        function.returnType
                    }
                }
            }
            is Expression.UseVariable -> {
                globalVariableDeclarations[expression.variableName]?.type ?: localVariables[expression.variableName] ?: throw TypeCheckerVariableNotFoundException(expression.variableName)
            }
            is Expression.Operation -> {
                if(expression.operator == Operator.Equals)
                    throw TypeCheckerOperationException("Can't use Operator at this position", expression.operator)

                if(expression.expressionB == null){
                    return when(expression.operator){
                        Operator.Not -> {
                            val typeA = getExpressionType(expression.expressionA, localVariables)
                            typeA as? Type.Boolean ?: throw TypeCheckerOperatorTypeException(Operator.Not,typeA)
                        }
                        Operator.Minus->  {
                            val typeA = getExpressionType(expression.expressionA, localVariables)
                            typeA as? Type.Integer ?: throw TypeCheckerOperatorTypeException(Operator.Minus,typeA)
                        }
                        else -> throw TypeCheckerOperationException("Needs more then one Argument", expression.operator)
                    }
                }
                else
                    return when (expression.operator){

                        Operator.And -> checkOperatorTypes<Type.Boolean>(expression.operator,expression.expressionA,expression.expressionB, localVariables)
                        Operator.Or -> checkOperatorTypes<Type.Boolean>(expression.operator,expression.expressionA,expression.expressionB, localVariables)

                        Operator.Minus -> checkOperatorTypes<Type.Integer>(expression.operator, expression.expressionA, expression.expressionB, localVariables)
                        Operator.Multiply -> checkOperatorTypes<Type.Integer>(expression.operator, expression.expressionA, expression.expressionB, localVariables)
                        Operator.Less -> checkOperatorTypes<Type.Integer>( Type.Boolean, expression.operator,expression.expressionA,expression.expressionB, localVariables)
                        Operator.LessEqual -> checkOperatorTypes<Type.Integer>(Type.Boolean, expression.operator,expression.expressionA,expression.expressionB, localVariables)
                        Operator.Greater -> checkOperatorTypes<Type.Integer>(Type.Boolean, expression.operator,expression.expressionA,expression.expressionB, localVariables)
                        Operator.GreaterEquals -> checkOperatorTypes<Type.Integer>(Type.Boolean, expression.operator,expression.expressionA,expression.expressionB, localVariables)

                        Operator.Plus -> checkOperatorTypes(listOf(Type.Integer,Type.String),expression.operator, expression.expressionA, expression.expressionB, localVariables)

                        Operator.DoubleEquals -> checkOperatorAllTypes(Type.Boolean,expression.operator, expression.expressionA, expression.expressionB, localVariables)
                        Operator.NotEqual -> checkOperatorAllTypes(Type.Boolean,expression.operator, expression.expressionA, expression.expressionB, localVariables)

                        Operator.Not -> throw TypeCheckerOperationException("Needs only one Argument", expression.operator)
                        Operator.Equals -> throw TypeCheckerOperationException("Can't use Operator at this position", expression.operator)
                    }
            }
        }
    }

    private fun <T> checkOperatorTypes(outputType : Type ,operator : Operator, expressionA: Expression, expressionB: Expression, localVariables : HashMap<String, Type>): Type {
        val typeA = getExpressionType(expressionA, localVariables)
        typeA as? T ?: throw TypeCheckerOperatorTypeException(operator, typeA)
        val typeB = getExpressionType(expressionB, localVariables)
        typeB as? T ?: throw TypeCheckerOperatorTypeException(operator, typeB)
        return outputType
    }

    private fun <T> checkOperatorTypes(operator : Operator, expressionA: Expression, expressionB: Expression, localVariables : HashMap<String, Type>): T {
        val typeA = getExpressionType(expressionA, localVariables)
        typeA as? T ?: throw TypeCheckerOperatorTypeException(operator, typeA)
        val typeB = getExpressionType(expressionB, localVariables)
        typeB as? T ?: throw TypeCheckerOperatorTypeException(operator, typeB)
        return typeA
    }

    private fun checkOperatorAllTypes(outputType : Type ,operator : Operator, expressionA: Expression, expressionB: Expression, localVariables : HashMap<String, Type>): Type {
        val typeA = getExpressionType(expressionA, localVariables)
        val typeB = getExpressionType(expressionB, localVariables)
        if(typeA == typeB)
            return outputType
        throw TypeCheckerOperatorTypeException(operator, typeA, typeB)
    }

    private fun checkOperatorTypes(validTypes : List<Type> ,operator : Operator, expressionA: Expression, expressionB: Expression, localVariables : HashMap<String, Type>): Type {
        val typeA = getExpressionType(expressionA, localVariables)
        val typeB = getExpressionType(expressionB, localVariables)

        if(typeA == typeB)
            if(validTypes.contains(typeA))
                return typeA
            else
                throw TypeCheckerOperatorTypeException(operator, typeA)
        else
            throw TypeCheckerOperatorTypeException(operator, typeA, typeB)
    }


}