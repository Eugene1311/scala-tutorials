package services

import javax.inject.Inject
import play.api.Configuration
import play.api.cache.AsyncCacheApi
import play.api.libs.json.JsValue
import wrappers.TwitterWebApi

import scala.concurrent.duration.Duration
import scala.concurrent.{ ExecutionContext, Future }

class TwitterSearchService @Inject()(twitterWebApi: TwitterWebApi,
                                     cache: AsyncCacheApi,
                                     configuration: Configuration,
                                     implicit val executionContext: ExecutionContext) {

  val cacheExpiry: Duration = configuration.get[Duration]("twitterCache.expiry")

  def searchRecent(twitterUser: String): Future[Map[String, JsValue]] = {
    cache.getOrElseUpdate[Map[String, JsValue]](twitterUser, cacheExpiry) {
      twitterWebApi.searchRecent(twitterUser).map(_.as[Map[String, JsValue]])
    }
  }
}
