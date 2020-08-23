//package mahout
//
//import org.apache.mahout.math.cf.SimilarityAnalysis
//import org.apache.mahout.sparkbindings.indexeddataset.IndexedDatasetSpark
//import org.apache.spark.{SparkConf, SparkContext}
//
//import org.apache.mahout.math.scalabindings.MahoutCollections._
//import collection._
//import JavaConversions._
//
//object LastFm {
//
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setMaster("local").setAppName("test")
//    val sc = new SparkContext(conf)
//    val dataPreffix= "/Users/zhangyugu/Downloads/hetrec2011-lastfm-2k/"
//
//    val userTagsRDDRow = sc.textFile(dataPreffix+"user_taggedartists.dat")
//    val userTagsRDD=  userTagsRDDRow.map(line=>line.split("\t"))
//      .map( a=> (a(0),a(2)) )
//      .filter(_._1 != "UserID")
//
//    val userTagsIDS = IndexedDatasetSpark.apply(userTagsRDD)(sc)
//
//    val userArtistsRDD = sc.textFile(dataPreffix+"/user_artists.dat")
//      .map(line=>line.split("\t"))
//      .map( a=> (a(0),a(1)) )
//      .filter(_._1 != "UserID")
//    val userArtistsIDS = IndexedDatasetSpark.apply(userArtistsRDD)(sc)
//
//    val userFriendsRDD = sc.textFile(dataPreffix+"user_friends.dat")
//      .map(line=>line.split("\t"))
//      .map( a=> (a(0),a(1)) )
//      .filter(_._1 != "UserID")
//    val userFriendsIDS = IndexedDatasetSpark.apply(userFriendsRDD)(sc)
//
//    val primaryIDS = userFriendsIDS
//    val secondaryActionRDDs = List(userArtistsRDD,userTagsRDD)
//
//    import org.apache.mahout.math.indexeddataset.{IndexedDataset, BiDictionary}
//    def adjustRowCardinality(rowCardinality:Integer, datasetA:IndexedDataset):IndexedDataset={
//      val returnedA = if(rowCardinality!=datasetA.matrix.nrow) datasetA.newRowCardinality(rowCardinality)
//      else datasetA
//      return returnedA
//    }
//
//    var rowCardinality = primaryIDS.rowIDs.size
//    val secondaryActionIDS:Array[IndexedDataset] = new Array[IndexedDataset](secondaryActionRDDs.length)
//    for(i<-secondaryActionRDDs.indices){
//      val bcPrimaryRowIDs = sc.broadcast(primaryIDS.rowIDs)
//      bcPrimaryRowIDs.value
//
//      val tempRDD = secondaryActionRDDs(i).filter(a=>bcPrimaryRowIDs.value.contains(a._1))
//
//      var tempIDS = IndexedDatasetSpark.apply(tempRDD,existingRowIDs = Some(primaryIDS.rowIDs))(sc)
//      secondaryActionIDS(i)=adjustRowCardinality(rowCardinality,tempIDS)
//    }
//
//    val artistReccosLlrDrmListByArtist = SimilarityAnalysis.cooccurrencesIDSs(
//      Array(userArtistsIDS,userTagsIDS,userFriendsIDS),
//      maxInterestingItemsPerThing = 20,
//      maxNumInteractions = 500,
//      randomSeed = 1234
//    )
//
//    val artistMap = sc.textFile(dataPreffix+"/artists.dat")
//      .map(line=>line.split("\t"))
//      .map( a=> (a(1),a(0)) )
//      .filter(_._1 != "name")
//      .collect
//      .toMap
//
//    val tagsMap = sc.textFile(dataPreffix+"/tags.dat")
//      .map(line=>line.split("\t"))
//      .map( a=> (a(1),a(0)) )
//      .filter(_._1 != "tagValue")
//      .collect
//      .toMap
//
//    val kilroyUserArtists = svec((userArtistsIDS.columnIDs.get(artistMap("Beck")).get,
//      (userArtistsIDS.columnIDs.get(artistMap("Beck")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("David Bowie")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Gary Numan")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Less Than Jake")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Lou Reed")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Parliament")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Radiohead")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Seu Jorge")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("The Skatalites")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Reverend Horton Heat")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Talking Heads")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Tom Waits")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Waylon Jennings")).get,1) ::
//        (userArtistsIDS.columnIDs.get(artistMap("Wu-Tang Clan")).get,1) :: Nil,
//        cardinality = userArtistsIDS.columnIDs.size
//      ))
//
//    val kilroyUserTags = svec(
//      (userTagsIDS.columnIDs.get(tagsMap("classical")).get,1) ::
//        (userTagsIDS.columnIDs.get(tagsMap("skacore")).get,1) ::
//        (userTagsIDS.columnIDs.get(tagsMap("why on earth is this just a bonus track")).get,1) ::
//        (userTagsIDS.columnIDs.get(tagsMap("punk rock")).get,1) :: Nil,
//
//      cardinality = userTagsIDS.columnIDs.size
//    )
//
//    val kilroysRecs = (artistReccosLlrDrmListByArtist(0).matrix %*% kilroyUserArtists +
//      artistReccosLlrDrmListByArtist(1).matrix %*% kilroyUserTags).collect
//
//
//    println(kilroysRecs(::,0).toMap.toList.sortWith(_._2 > _._2).take(5))
//  }
//
//}
