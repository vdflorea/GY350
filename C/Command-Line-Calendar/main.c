#include <stdio.h>
#include <stdlib.h>

void genCalendar(int month, int year);
int isLeapYear(int year);
int newYearsDay(int year);
int numDays(int year, int month);
int firstDay(int year, int month);

int main(int argc, char *argv[]) {
    // The user supplies three arguments:
    // arg[1]: year number
    // arg[2+]: consecutive month arguments

    int year = atoi(argv[1]);

    if (year < 1900) {
        return 0;
    }

    for (int i = 0; i < (argc - 2); i++) {
        genCalendar(atoi(argv[i+2]), year);
    }

}

void genCalendar(int month, int year) {
    // Print out a calendar

    int newYear = newYearsDay(year);
    int fDay = (newYear + firstDay(year, month)) % 7; // 0 --> 6 (Day that specific month starts on)
    int numMonthDays = numDays(year, month); // 28 --> 31 (Number of Days in specific month)
    int currCell = 1;

    printf("\n\n%d/%d\n", month, year);
    printf("\n Sun Mon Tue Wed Thu Fri Sat\n");

    while (currCell <= fDay) {
        // Print out white spaces up until the first day of the month
        printf("    ");
        currCell++;
    }

    while ((currCell - fDay) <= numMonthDays) {
        // Print out all "day" cells up until the number of days in the specific month
        printf("%4d", (currCell - fDay));
        if (currCell % 7 == 0) {
            printf("\n");
        }
        currCell++;
    }
}

int isLeapYear(int year) {
    // Checks if year is a leap year according to rules below
    if (year % 400 == 0) {
        return 1;
    }

    if (year % 100 == 0) {
        return 0;
    }

    if (year % 4 == 0) {
        return 1;
    }

    return 0;
}

int newYearsDay(int year) {
    // Return day of the week (0-6, where Sun=0) that New Year's Day falls on for the given year
    int daysFrom1900 = 0;

    for (int i = 1900; i < year; i++) {
        daysFrom1900 += 365 + isLeapYear(i);
    }

    return ((1 + daysFrom1900) % 7);
}

int numDays(int year, int month) {
    // Returns number of days in specific month of specific year
    if (month == 9 || month == 4 || month == 6 || month == 11) {
        return 30;
    }
    if (month != 2) {
        return 31;
    }
    return (28 + isLeapYear(year));
}

int firstDay(int year, int month) {
    // Returns the days from the 1st of January of supplied year --> 1st of specific Month of supplied year
    int days = 0;

    for (int i = 1; i < month; i++) {
        days += numDays(year, i);
    }

    return days;
}
