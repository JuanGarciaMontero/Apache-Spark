package com.openwebinars.twitter

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.Seconds

object ingesta extends App {
  
  System.setProperty("twitter4j.oauth.consumerkey", "jpNQlAxDcJLzg5L0NK5C9QIzG")
  System.setProperty("twitter4j.oauth.consumerSecret", "KX8vvNARIxODOos9qflSqDkpI1h0kgUh48lvIiIsIKEr6k8SXz")
  System.setProperty("twitter4j.oauth.accessToken", "12390402-WEGGjnPCeEb0pIq6uo21EeEsVzM28rxgypTUHPX1N")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "VCQtY0W33PH6izj0UAw1Mq1kdFu2pK3WiBMPCHWP4mIo7")
  
  val scc = new StreamingContext("local[*]", "IngestaTweets", Seconds(60))
  
  val tweets = TwitterUtils.createStream(scc, None)
  
  
  val texto = tweets.map(status => status.getText())
  
  texto.print()
  
  scc.start()
  scc.awaitTermination()
 
}