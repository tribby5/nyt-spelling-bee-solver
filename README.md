# nyt-spelling-bee-solver

The goal of this repo is to create a list of possible words that would be used by the NYT Spelling Bee Game. In the game, the player is given seven letters, one of  them being the central, highlighted letter. The goal is to create as many words as possible and all the words must include that central letter and be at least four characters long. The result of this repo is at data/possible_words.parquet.

## Usage
Simple sbt project. Package with `sbt package` then can do a spark-submit to actually run the job locally (can substitute the master with a real cluster if you have that set up)
`$SPARK_HOME/bin/spark-submit --class "Preprocessor" --master local target/scala-2.12/nyt-spelling-bee-solver_2.12-1.0.jar`

## Process
The data used is derived from [Peter Norvig's compilation of the 1/3 million most frequent English words](https://norvig.com/ngrams). From there, I filtered out all words that are less than four characters long. For further cleaning, I joined to a list of scrabble dictionary approved words because some of the words in the original dataset weren't real words / were close misspellings of real words (i.e. 'wwwskincare', 'hallpween'). I also filtered out all words that contained more than 7 unique characters (as that would be impossible in this game structure). This brings us all the way down to ~50,000 words - this seems like a good starting point. 

## Extra Data Columns
Added a WORD_KEY column - this is going to be used for an efficient solution for a solver. The 'WORD_KEY' of a word is simply the distinct characters in the word alphabetically sorted so 'aardvarks' would become 'adkrsv'.

## Further Improvement
The NYT Spelling Bee game editor does not use more obscure words like intense medical terms. There could be more work done to further filter out these obscure words. One idea would be to have a frequency cutoff on the words. However, in my experience using the dataset, sometimes less frequent words work while a more frequent word doesn't. So there doesn't seem to be an obvious way to do a frequency cutoff effectively. I would rather have an dataset that gives too many words rather than one that misses words. 
