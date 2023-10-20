package com.openwebinars.twitter

import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils

object HashtagCount extends App {
  System.setProperty("twitter4j.oauth.consumerkey", "jpNQlAxDcJLzg5L0NK5C9QIzG")
  System.setProperty("twitter4j.oauth.consumerSecret", "KX8vvNARIxODOos9qflSqDkpI1h0kgUh48lvIiIsIKEr6k8SXz")
  System.setProperty("twitter4j.oauth.accessToken", "12390402-WEGGjnPCeEb0pIq6uo21EeEsVzM28rxgypTUHPX1N")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "VCQtY0W33PH6izj0UAw1Mq1kdFu2pK3WiBMPCHWP4mIo7")
  
  val ssc = new StreamingContext("local[*]", "Hastagcount", Seconds(60))
   
  val tweets = TwitterUtils.createFilteredStream(ssc, None)
  
  val tweetsSpain = tweets.filter(status => status.getLang.equals("es"))
  
  val hashtags = tweetsSpain.flatMap(status => status.getText().split(" ").filter(_.startsWith("#")))
  
  val hashtagsCount = hashtags.map(h => (h,1)).reduceByKey(_+_).transform(_.sortBy(_._2, false))
  
  hashtagsCount.print()
  
  ssc.start()
  ssc.awaitTermination()
  
}