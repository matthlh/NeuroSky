This is my code samples for the Greater Vancouver Regional Science Fair where my partner and I analyzed the brainwaves of when a person is drowsy.

The files ButtonPresser, ConnectListener, ControlLayout, and StartListener make up the game awareness game. This game consists of blinking arrow, and the user is supposed to press the correct arrow key when it flashes. Awarding different points to the user if either the correct key was pressed, no keys were pressed or if the wrong array was pressed. Graphing the points vs time out we could identify when a person was awake, drowsy or asleep. 

The game wouldn't load if the code was not recieving any brainwave data. The NeuroSocket and OnEEGDataListener files opens a socket in my computer and connects to an application called MindWave Mobile Tutorial. This application via bluetooth recieved the data from a NeuroSky electroencephalogram(EEG) headset. With a client and server connection, I am able to recieve different brainwaves in my code.

The remaining file GraphListener, is used to graph the the power of the brainwaves vs time, for users to get a feeling of what mental state they are in. For example, calm, focused, drowsy, asleep.
