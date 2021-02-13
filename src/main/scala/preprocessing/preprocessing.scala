
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

object Preprocessor {

    val wordListSchema = StructType(Array(
    StructField("WORD",StringType,false),
    StructField("FREQUENCY",LongType,false),
    ))

    val scrabbleListSchema = StructType(Array(
        StructField("WORD",StringType,false)
    ))

    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()


    def generateListOfPossibleWords(): Unit = {
        val wordList = spark.read.schema(wordListSchema).csv("data/original_word_list.csv")
        val scrabbleWords = spark.read.schema(scrabbleListSchema).csv("data/scrabble_dictionary.csv")

        val possibleWords = wordList
            .transform(filterByWordLength)
            .join(scrabbleWords, Seq("WORD"), "inner")
            .transform(withWordKey)
            .transform(filterByNumUniqueChars)
    
        possibleWords.write.parquet("data/possible_words.parquet")

    }

    def filterByWordLength(df: DataFrame): DataFrame = {
        df.filter(length(col("WORD")) >= 4)
    }

    def withWordKey(df: DataFrame): DataFrame = {
        val getUniqueCharacters = (word: String) => {
        word.distinct.sorted
        }

        val getUnique = spark.udf.register("uniqueChars", getUniqueCharacters)

        df.withColumn("WORD_KEY", getUnique(col("WORD")))
    }

    def filterByNumUniqueChars(df: DataFrame): DataFrame = {
        df.withColumn("NUM_UNIQUE_CHAR", length(col("WORD_KEY")))
            .filter(col("NUM_UNIQUE_CHAR") < 8)
            
    }

    def main(args: Array[String]) {
        generateListOfPossibleWords()
        spark.stop()
    }
}




