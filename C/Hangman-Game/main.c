#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>

#define MAXSTRING 100
FILE *file_ptr;
char txt[100000][MAXSTRING]; // String for reading each line into

void hangmanGame(int);

void main() {
    fopen_s(&file_ptr, "C:\\Users\\Vlad\\Desktop\\dictionary.txt", "r"); // Open for reading

    if (file_ptr == NULL) {
        printf("Could not open dictionary.txt");
    } else {
        int lines = 0;
        while (fgets(txt[lines], MAXSTRING-1, file_ptr) != NULL) {
            if ((strlen(txt[lines])-1) < 4 || (strlen(txt[lines])-1) > 7) {
                // Rejects words less than 4 / more than 7 letters
                continue;
            }
            txt[lines][strlen(txt[lines])-1] = '\0'; // Remove the \n which has also been read into the string!
            printf("%s\t", txt[lines]);
            lines++;
        }

        printf("\n\n-----------------------------------\n");
        printf("dictionary.txt contained %d lines.\n", lines);
        printf("-----------------------------------\n");
        fclose (file_ptr);

        hangmanGame(lines); // Start the game!
    }
}

void hangmanGame(int numWords) {
    srand(time(NULL));
    int randomWord = rand() % numWords;
    int randWordLength = strlen(txt[randomWord]); // Length of the random word
    int numGuesses = 0;
    char displayWord[randWordLength];
    char guessWord[randWordLength];
    char userInput[100];
    char userLetter;
    strcpy_s(guessWord, MAXSTRING, txt[randomWord]); // Copies the random word at its index in txt --> guessWord

    for (int i = 0; i < randWordLength; i++) {
        // Initialises displayWord with the amount of hyphens corresponding to the random word's length
        displayWord[i] = '-';
    }
    displayWord[randWordLength] = '\0'; // Ensure that \0 at end of displayWord string for comparison test later

    while (strcmp(displayWord, guessWord) != 0) { // strcmp returns 1 if strings are NOT the same
        printf("\n-==Guess (%d)==-", numGuesses + 1);
        printf("\n     %s", displayWord);
        printf("\nGuess a SINGLE letter >> ");
        gets(userInput); // Gets all of the user's input (many characters etc.)
        userLetter = userInput[0]; // Only takes/uses the first character they typed
        //scanf_s(" %c", &userLetter); (Alternatively can use this!)
        for (int i = 0; i < randWordLength; i++) {
            if (guessWord[i] == userLetter) {
                // Swapping each '-' for the user's letter if applicable
                displayWord[i] = userLetter;
            }
        }
        numGuesses++;
    }

    printf("\n\nThe word was %s! You got it after %d guesses!", displayWord, numGuesses);
}
