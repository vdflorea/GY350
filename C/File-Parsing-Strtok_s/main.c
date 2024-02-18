
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
    char make[20];
    char model[20];
    int year;
}car;

car garage[10];
FILE* fptr;
void readCars(char myfilePath[], int numCars);
void displayGarage(int numCars);
int checkYear(int numCars, int year);

void main() {
    char myfilePath[] = "C:\\Users\\Vlad\\Desktop\\carsYear.csv";
    printf("--------------- Cars in Garage ---------------\n");
    readCars(myfilePath, 10);
    displayGarage(10);
    int ans = checkYear(10, 2016);
    printf("There are %d cars with year %d in the garage.\n",ans,2016);
}

void readCars(char myfilePath[], int numCars) {
    char line[30];
    char delims[]= " ,"; // Two possible delimiters: " " and ","
    char* first;
    char* next = NULL; // Important to initialise next as NULL at the start
    int currCar = 0;
    car c;
    fopen_s(&fptr, myfilePath, "r"); // Open CSV file in read mode

    if (fptr != NULL) {
        while (!feof(fptr) && currCar < numCars) { // Two conditions ensure correct parsing of data
            fgets(line, 30, fptr); // Gets the whole line
            first = strtok_s(line, delims, &next); // first becomes the make of the car
            strcpy_s(c.make, 20, first); // Copying the make of the current car to the make variable in the car struct from first
            first = strtok_s(NULL, delims, &next); // first becomes the model of the car
            strcpy_s(c.model, 20, first); // Copying the model of the current car to the model variable in the car struct from first
            first = strtok_s(NULL, delims, &next); // first becomes the year of the car (string)
            c.year = atoi(first); // Converting the year of the car from a string --> integer and placing it in the year variable in the car struct

            garage[currCar] = c; // The current car's details (make, model and year variables) are transferred to the current car's respective index in the garage array
            currCar++; // Increment to the next car for the next iteration of the while loop
        }

    } else {
        printf("Error opening file: Exiting!");
    }
    fclose(fptr);
}

void displayGarage(int numCars) {
    for (int j = 0; j < numCars; j++) {
        // Prints out each of the car's details by referencing each detail of the current car in the garage array
        printf("Car Number: %d\n", j+1);
        printf("Car Make: %s\n", garage[j].make);
        printf("Car Model: %s\n", garage[j].model);
        printf("Car Year: %d\n", garage[j].year);
        printf("\n--------------------\n");
    }
}

int checkYear(int numCars, int year) {
    int ctr = 0;
    for (int j = 0; j < numCars; j++) {
        if (garage[j].year == year) {
            ctr++; // Counter increments each time the year variable of the current car being checked is 2016
        }
    }
    return ctr;
}
