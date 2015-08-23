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
  if (size > 0) {
    byte* msg = (byte*)malloc(size + 1);
    int len = udp.read(msg, size + 1);
    msg[len] = 0;
    byte* now = msg;
    cmd c [10];
    memset(&c, 0, sizeof(cmd) * 10);
    byte i = 0;
    while (*now != 0) {
      c[i].typ = *now;
      now++;
      c[i].pin = *now;
      now++;
      c[i].val = *now;
      now++;
      i++;
    }
    free(msg);
    for (byte j = 0; j < i; j++) {
      Serial.printf("X %d %d %d X\n", c[j].typ , c[j].pin , c[j].val);
    }

    udp.stop();
    Serial.println (udp.begin(8000) ? "success" : "failed");
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
