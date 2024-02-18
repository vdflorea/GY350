#include <stdio.h>
#include <string.h>
#include <ctype.h>

FILE *file_ptr;
char allText[200000];
char oneLine[1000];

void readFromFile(char []);
void parseText(int*, int*, int*);
int isVowel(char);
int isStopChar(char);
int isNewWord(char);
float fleschIndexCalc(float, float, float);

void main() {
    char greenEggs[1000] = "C:\\Users\\Vlad\\Desktop\\article-green-eggs-and-ham.txt";
    char irishTimes[1000] = "C:\\Users\\Vlad\\Desktop\\article-irish-times.txt";
    int userInput = 2;

    while (userInput != 1 && userInput != 0) { // Ensures that user chooses one of the two file paths
        printf("Enter which file you wish to calculate the Flesch Readability Index on :\n");
        printf("1 --> article-green-eggs-and-ham.txt\t0 --> article-irish-times.txt\n");
        printf(">>");
        scanf_s("%d", &userInput);
    }

    int numWords, numSyllables, numSentences;

    readFromFile(userInput ? greenEggs : irishTimes); // 1 --> article-green-eggs-and-ham.txt // 0 -->article-irish-times.txt

    parseText(&numWords, &numSyllables, &numSentences); // Send variable pointers to function

    printf("\n\nWords: %d\nSyllables: %d\nSentences: %d\n\n", numWords, numSyllables, numSentences);

    float fleschIndex = fleschIndexCalc((float)numWords, (float)numSyllables, (float)numSentences);

    printf("Flesch Readability Index Calculation ==> %f", fleschIndex);
}

void readFromFile(char path[]) {
    fopen_s(&file_ptr, path, "r");

    if (file_ptr == NULL) {
        printf("Could not open article\n");
    }
    else {
        allText[0] = '\0'; // Make sure this string is empty
        while (fgets(oneLine, 999, file_ptr)!=NULL) {
            // Read the next line and append it (with \n intact)
            strcat(allText, oneLine);
            // Concatenate each line together
        }

        printf("\n%s", allText);

        fclose (file_ptr);
    }
}

void parseText(int* words, int* syllables, int* sentences) {
    int currChar = 0;

   while (allText[currChar] != '\0') {
       if (isNewWord(allText[currChar]) && !isNewWord(allText[currChar - 1])) {
           // Checks for spaces and '\n' characters
           // If there are a few '\n' characters or spaces in a row, words will not be incremented
           (*words)++;
       }

       if (isVowel(allText[currChar])) {
           if (!isVowel(allText[currChar+1])) {
               // Ensures that if there are multiple vowels in a row, syllables will only increment
               // on the last vowel before a non-vowel character is met
               (*syllables)++;
           }
       }

       if (isStopChar(allText[currChar])) {
           // Increments sentences when a stop character is met
           (*sentences)++;
       }

       if (tolower(allText[currChar]) == 'e' && (isNewWord(allText[currChar + 1]) || isStopChar(allText[currChar + 1]))) {
           // Ensures that any word that ends in an 'e' character is not counted as a syllable
           (*syllables)--;
       }
       currChar++;

   }
}

int isNewWord(char letter) {
    switch (letter) {
        case ' ':
            return 1;
        case '\n':
            return 1;
        case '\t':
            return 1;
        default:
            return 0;
    }
}

int isVowel(char letter) {
    letter = tolower(letter);

    switch (letter) {
        case 'a':
            return 1;
        case 'e':
            return 1;
        case 'i':
            return 1;
        case 'o':
            return 1;
        case 'u':
            return 1;
        case 'y': // Not a vowel but oh well :)
            return 1;
        default:
            return 0;
    }
}

int isStopChar(char currLetter) {
    switch (currLetter) {
        case '.':
            return 1;
        case ':':
            return 1;
        case ';':
            return 1;
        case '?':
            return 1;
        case '!':
            return 1;
        default:
            return 0;
    }
}

float fleschIndexCalc(float words, float syllables, float sentences) {
    return 206.835 - (84.6 * (syllables / words)) - (1.015 * (words / sentences)); // Returns computed Flesch Index
}