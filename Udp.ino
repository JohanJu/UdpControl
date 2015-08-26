/*
cmd typ: 0 = pinIn, 1 = pinOut, 2 = dRead, 3 = aRead 4 = dWrite;
Lib from https://github.com/ntruchsess/arduino_uip
*/

#include <SPI.h>
#include <UIPEthernet.h>

EthernetUDP udp;

typedef struct cmd {
  byte typ;
  byte pin;
  byte val;
} cmd;

void update() {
  int size = udp.parsePacket();
  if (size%3 == 2) {
    byte i = 0, oldSum = 0, newSum = 0, flag = 0xaa;
    byte* msg = (byte*)malloc(size + 1);
    int len = udp.read(msg, size + 1);
    msg[len] = 0;
    byte* now = msg;
    if (*now != flag) {
      free(msg);
      Serial.println ("wrong flag");
      udp.stop();
      udp.begin(8000);
      return;
    }
    now ++;
    oldSum = *now;
    now ++;
    cmd c [8];
    memset(&c, 0, sizeof(cmd) * 8);
    while (*now != 0) {    
      c[i].typ = *now;
      newSum += *now;
      now++;
      c[i].pin = *now;
      newSum += *now;
      now++;
      c[i].val = *now;
      newSum += *now;
      now++;
      i++;
    }
    free(msg);
    if (oldSum != newSum) {
      Serial.println (oldSum);
      Serial.println (newSum);
      Serial.println ("wrong checksum");
      udp.stop();
      udp.begin(8000);
      return;
    }
    for (byte j = 0; j < i; j++) {
      Serial.printf("X %d %d %d X\n", c[j].typ , c[j].pin , c[j].val);
    }
    udp.stop();
    udp.begin(8000);
  } else if (size > 0) {
    Serial.println ("wrong size");
    udp.stop();
    udp.begin(8000);
  }

}

void setup() {

  Serial.begin(9600);
  Serial.println("Starting");
  byte mac[] = {
    0xaa, 0xaa, 0xaa, 0xaa, 0xaa, 0xaa
  };

  if (Ethernet.begin(mac) == 0) {
    Serial.println("DHCP err");
    for (;;)
      delay(1000);
  } else {
    Serial.printf("%d.%d.%d.%d\n", Ethernet.localIP()[0], Ethernet.localIP()[1], Ethernet.localIP()[2], Ethernet.localIP()[3]);
    Serial.println(udp.begin(8000) ? "success" : "failed");
  }
}
void loop() {
  update();
  delay(100);
}
