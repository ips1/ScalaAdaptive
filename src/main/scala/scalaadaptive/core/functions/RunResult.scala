package scalaadaptive.core.functions

/**
  * Created by Petr Kubat on 5/20/17.
  *
  * Result of a function selection and invocation containing the return value and the run data.
  *
  * @param value The return value of the invoked option.
  * @param runData The description of the selection and invocation process, see [[RunData]].
  *
  */
class RunResult[TResultType](val value: TResultType,
                             val runData: RunData)
