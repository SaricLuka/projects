#include "SPI.h" // SPI library
#include "MFRC522.h" // RFID library (https://github.com/miguelbalboa/rfid)

const int pinRST = 9;
const int pinSDA = 10;
const int pinB = 3;
const int pinR = 5;
const int pinG = 6;

char sta = 'z';

MFRC522 mfrc522(pinSDA, pinRST); // Set up mfrc522 on the Arduino

void setup() {
  pinMode(pinB, OUTPUT);
  pinMode(pinR, OUTPUT);
  pinMode(pinG, OUTPUT);

  analogWrite(pinR,255);
  analogWrite(pinG,0);
  analogWrite(pinB,0);

  SPI.begin(); // open SPI connection
  mfrc522.PCD_Init(); // Initialize Proximity Coupling Device (PCD)
  Serial.begin(9600); // open serial connection
}

void loop() {
  if (mfrc522.PICC_IsNewCardPresent()) { // (true, if RFID tag/card is present ) PICC = Proximity Integrated Circuit Card
    if(mfrc522.PICC_ReadCardSerial()) { // true, if RFID tag/card was read
      for (byte i = 0; i < mfrc522.uid.size; ++i) { // read id (in parts)
        Serial.print(mfrc522.uid.uidByte[i], HEX); // print id as hex values
        Serial.print(" "); // add space between hex blocks to increase readability
      }
      Serial.println(); // Print out of id is complete.
      delay(500);
      if(Serial.available() > 0){
        if(Serial.read() == 'o'){
          analogWrite(pinR,0);
          analogWrite(pinG,255);
          analogWrite(pinB,255);
          sta = 'o';
        }
      }
        delay(5000);
        analogWrite(pinR,255);
        analogWrite(pinG,0);
        analogWrite(pinB,0);
        sta = 'z';
    }
  }
}
