# ASR_Sonopoly

## Setup
  1. Download Asr model from https://github.com/wPatchanon/ASR_deploy.
  2. Run model and start server with Gstreamer.
  3. Open code project with your editor.
  4. Set the path of a recording file in **src/Listener/JavaSoundRecorder.java** and **src/Listener/Decoder.java** (Same path)
  5. Set Gstream server URL in **src/Listener/Decoder.java**
  6. Run program.

## How to play?
  1. Press Space-Bar to start recording, wait 2 seconds before speaking because JavaRecorder have some delay.
  2. Press Space-Bar again to stop recording, the program will show an output command in a terminal and execute this command.
  3. **Example commands:** เริ่มเกม, จ่ายเงิน, ซื้อโฉนด, ขายโฉนด, ยอมแพ้, ทอยลูกเต๋า. (See more in the transcript)
