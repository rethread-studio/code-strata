let breathIn;
let breathOut;
let interval = 2000; // interval between grains

function preload() {
    breathIn = loadSound('../sounds/breath_in.wav');
    breathOut = loadSound('../sounds/breath_out.wav');
}

function player() {
    let grainDuration = random(0.2, breathIn.duration());
    let offset = random(0, 0.2); // beginning of the sample
    breathIn.play(0, 1, 1, offset, grainDuration);

    let playerTimeout = setTimeout(player, interval);
}

function setup() {
    breathIn.playMode('sustain');
    player();
}