# :spider::cat::hourglass: weaver-cats-retry

_A proof-of-concept enriching [weaver-test](https://typelevel.org/weaver-test/)'s flexibility using a [cats-effect](https://typelevel.org/cats-effect/)-adjacent library, [cats-retry](https://github.com/cb372/cats-retry)._

### Usage
1. Implement your own suite or use the included `RandomSuite` extending the added `RetryingIOSuite` to make it retry!
2. Override or use the default `RetryPolicy` passed into `RetryingIOSuite` to manage the timing, backoff, and thresholds of the retrying.
3. Test this new functionality with: `sbt test`