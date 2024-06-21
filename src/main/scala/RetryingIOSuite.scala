import scala.Console._
import scala.concurrent.duration.FiniteDuration

import cats.effect.IO

import retry.RetryDetails._
import retry.{RetryDetails, RetryPolicies, RetryPolicy, retryingOnFailures}
import weaver._

class RetryingIOSuite(
    policy: RetryPolicy[IO] = RetryPolicies.limitRetries[IO](5)
) extends SimpleIOSuite {

  override protected def registerTest(
      name: TestName
  )(f: Res => IO[TestOutcome]): Unit =
    super.registerTest(name)(res =>
      retryingOnFailures[TestOutcome](
        policy = policy,
        wasSuccessful = testPassed,
        onFailure = logFailure
      ) {
        for { outcome <- f(res) } yield outcome
      }
    )

  def testPassed(testOutcome: TestOutcome): IO[Boolean] = {
    IO(
      !Set(TestStatus.Failure, TestStatus.Exception)
        .exists(status => status == testOutcome.status)
    )
  }

  def logFailure(testOutcome: TestOutcome, details: RetryDetails): IO[Unit] = {
    details match {
      case WillDelayAndRetry(_, retries: Int, elapsed: FiniteDuration) =>
        for {
          _ <- IO.println(
            format(s"Test[${testOutcome.name}] failed...", BOLD, UNDERLINED)
          )

          _ <- IO.println(
            (
              testOutcome match {
                case TestOutcome.Default(_, _, result, _) =>
                  result.formatted
                case _ => None
              }
            ).getOrElse(testOutcome.log)
          )

          _ <- IO.println(
            format(
              retryMessage(willRetry = true, retries, elapsed),
              BOLD
            )
          )
        } yield ()

      case GivingUp(totalRetries: Int, totalDelay: FiniteDuration) =>
        IO.println(
          format(
            retryMessage(willRetry = false, totalRetries, totalDelay),
            BOLD
          )
        )
    }
  }

  def retryMessage(
      willRetry: Boolean,
      retries: Int,
      elapsed: FiniteDuration
  ): String =
    s"${if (willRetry) "Retrying" else "Giving up"} " +
      s"after $retries attempts, and a time elapsed of ${elapsed.toMinutes} minute(s)..."

  def format(text: String, colors: String*): String =
    colors.mkString("") + text + RESET
}
