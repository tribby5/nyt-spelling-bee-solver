# nyt-spelling-bee-solver

## Data:
The dictionary used is derived from Peter Norvig's compilation of the 1/3 million most frequent English words. There was some additional cleaning required on the original dataset to filtering out words that weren't real English words (i.e. 'hallpween' and 'wwwskincare'). I accomplished this by joining with a scrabble dictionary of words From those words, I filtered out all words that would never fit into a NYT Spelling Bee Parameters: words that are less than 4 characters long, words that use more than 8 unique letters.


aardwolves in scrabble dictionary 
down to ~50,000 words - this seems like a good starting point 