import scala.util.Random

import cats.effect.{IO, _}

object RandomSuite extends RetryingIOSuite {

  test("Randomly select equal numbers") {
    for {
      x <- IO(Random.nextInt(5))
      y <- IO(Random.nextInt(5))
      _ <- IO.println(s"Checking if $x == $y...")
    } yield expect(x == y)
  }
}