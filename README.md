ADC
===
ADC, Android Development Course, is a series of Android development classes taught by Evan Liu (hmisty) in Beihang University Software College, Beijing, China.

ADC Around Me
---
ADC Around Me is the 9th homework of the ADC.
The source code here is a partially finished Android application. The goal for the students is to complete the application as required.

The Source Codes
---
AroundMe/ : the android client
chat_server/ : the chat server. how to run:
  1. install erlang
  1. $ erl
  1. > c(chat_server1).
  1. > chat_server1:start(4000).
couchdb view: _design/stars { "views": { "by_loc": { "map": "function(doc){if(doc.type==\"star\") emit([doc.long,doc.lat],doc)}" } } } 

Homework
===

Requirements
---
There are four requirements of the homework:

### Fix the calculation of time left

### Add the account, login and authentication

### Implement the input of sex and mobile phone
Also implement the phone call.

### Improve the couchdb view to not select out the expired data
Donot select out them and donot show them on the map.

Submission
---
  * Source codes
  * Document
  * Screenshots

HowTo
---
Fork, complete, commit, push and send me a pull request at hmisty/adc_aroundme.

The MIT License
---
Copyright (c) 2012
Evan (Qingyan) Liu

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
