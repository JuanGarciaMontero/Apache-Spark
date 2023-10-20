package com.openwebinars.twitter

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.twitter.TwitterUtils
import java.util.Date

// 2. Vamos a quedarnos con los 10 tweets de los usuarios con más followers y los mostraremos de nuevo en
// un fichero.Volveremos a realizar este análisis con una ventana de 90 segundos.

object Ejercicio2 extends App {
  
  System.setProperty("twitter4j.oauth.consumerkey", "jpNQlAxDcJLzg5L0NK5C9QIzG")
  System.setProperty("twitter4j.oauth.consumerSecret", "KX8vvNARIxODOos9qflSqDkpI1h0kgUh48lvIiIsIKEr6k8SXz")
  System.setProperty("twitter4j.oauth.accessToken", "12390402-WEGGjnPCeEb0pIq6uo21EeEsVzM28rxgypTUHPX1N")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "VCQtY0W33PH6izj0UAw1Mq1kdFu2pK3WiBMPCHWP4mIo7")
  
  val ssc = new StreamingContext("local[*]", "TTSpain", Seconds(30))
  
  val tweets = TwitterUtils.createStream(ssc,None)
  val tweetsWindows = tweets.window(Seconds(90))
  
  case class Tweet(created_at: Date, user: String, texto: String, followers: Int)
  
  val mytweets = tweetsWindows.filter(_.getLang.equals("es")).
          map(t => Tweet(t.getCreatedAt(), t.getUser.getScreenName, t.getText, t.getUser.getFollowersCount))
  
  val orderFollowers = mytweets.transform(_.sortBy(_.followers, false))
  
  val orderTen = mytweets.transform( rdd => {
    val list = rdd.sortBy(_.followers, false).take(10);
    rdd.filter(list.contains)
  }
  )
 
  orderFollowers.saveAsTextFiles("/Users/jgmen/Desktop/Followers", "csv")
  
  
  ssc.start()
  ssc.awaitTermination()
  
  
}