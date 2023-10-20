package com.openwebinars.twitter

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.twitter.TwitterUtils
import java.util.Date


// 1. Vamos a guardar en un fichero aquellos tweets con el hashtag que sea trending topic en España en estos
// momentos. Realizaremos este análisis con una ventana de 90 segundos.
object Ejercicio1 extends App {
  
  System.setProperty("twitter4j.oauth.consumerkey", "jpNQlAxDcJLzg5L0NK5C9QIzG")
  System.setProperty("twitter4j.oauth.consumerSecret", "KX8vvNARIxODOos9qflSqDkpI1h0kgUh48lvIiIsIKEr6k8SXz")
  System.setProperty("twitter4j.oauth.accessToken", "12390402-WEGGjnPCeEb0pIq6uo21EeEsVzM28rxgypTUHPX1N")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "VCQtY0W33PH6izj0UAw1Mq1kdFu2pK3WiBMPCHWP4mIo7")
  
  val ssc = new StreamingContext("local[*]", "TTSpain", Seconds(30))
  
  val tweets = TwitterUtils.createStream(ssc,None) 
  val tweetsWindows = tweets.window(Seconds(90))
  
  case class Tweet(created_at: Date, texto: String)
  
  val espanol = tweetsWindows.filter(status => status.getLang.equals("es")).
                        map(status => Tweet(status.getCreatedAt(), status.getText()))
      
  val tt = espanol.filter(_.texto.split(" ").contains("Isabel"))
  
  tt.saveAsTextFiles("/Users/jgmen/Desktop/TT", "csv")
  
  ssc.start()
  ssc.awaitTermination()
 
                        
}