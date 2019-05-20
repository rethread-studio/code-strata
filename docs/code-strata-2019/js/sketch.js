const Config = {
  FRAMES_TO_EXPLODE: 120, // Controls the number of frames a strata is visible
  PARTICLE_PADDING: 5, // Minimum distance between particles
  REPULSION_STRENGTH: -2000, // Strength of the repulsion between particles. Must be negative.
  JITTER: 0.0001, // Jitter for the repulsion
  MIN_SPEED: 2, // Minimum speed for a particle
  DRAG: 0.03, // Drag for particles

  INITIAL_BACKKGROUND_COLOR: 255,
  BACKGRUND_COLOR_VARIATION: 3,

  MOVING_PARTICLE_COLOR: 0,
  LOCKED_PARTICLE_COLOR: 255,

  SOUND_INTERVAL: 4000,
  SOUND_FRAGMENT: 0.7, // maximum grain duration and minimum offset

  BACKWARD_FRAME_RATE: 28,
  FORWARD_FRAME_RATE: 60,
};


let physics;
let data;
let colorBackground = Config.INITIAL_BACKKGROUND_COLOR;

let animation = {
  direction: 1,
  currentSnapshot: 0,
}


let snapshots = [];

function isMovingForward() {
  return animation.direction > 0;
}

function shouldMoveBackwards() {
  //Stratas are over, so we should go back
  return physics.particles.length == 0;
}

function shouldMoveForward() {
  //Finished presenting all snapshots
  return animation.currentSnapshot == 0;
}

function changeDirection() {
  if (isMovingForward()) {
    //Now move backwards
    animation.direction = -1;
    animation.currentSnapshot = snapshots.length;
    Assets.Sounds.BreathIn.stop();
    Assets.Sounds.BreathOut.play();
    frameRate(Config.BACKWARD_FRAME_RATE);
  } else {
    // Should move forward
    animation.direction = 1;
    animation.currentSnapshot = 0;
    //Assets.Sounds.BreathOut.stop();
    //player()
    snapshots = [];
    initPhysics();
    frameRate(Config.FORWARD_FRAME_RATE);
    colorBackground = Config.INITIAL_BACKKGROUND_COLOR;
  }
}

// Map from teams to fonts
const Team2FontMap = {
  'App': 'Courier',
  'javafx': 'Helvetica'
}


let frozzen_particles = [];

function preload() {

  loadAssets();

  data = new Data(
    loadJSON('../data/data.json'),
    loadJSON('../data/developers.json')
  );


}

function setup() {
  setupVisuals();
  setupPhysics();
  setupPlayer();
}

function setupVisuals() {
  createCanvas(windowWidth, windowHeight);
  textAlign(CENTER);
}

function setupPhysics() {
  // Physical system
  physics = new VerletPhysics2D();
  physics.setDrag(Config.DRAG);
  physics.setWorldBounds(new Rect(0, 0, windowWidth, windowHeight));

  initPhysics();
}

function initPhysics() {
  // Stratas will be represented as particles
  // The first strata is one particle with the root element
  physics.particles = [
    new Item(new Vec2D(windowWidth / 2, windowHeight / 2), data.root)
  ];
}

function setupPlayer() {
  Assets.Sounds.BreathIn.playMode('sustain');
  player();
}

function player() {
  let grainDuration = random(0.3, Config.SOUND_FRAGMENT);
  let offset = random(Config.SOUND_FRAGMENT, Assets.Sounds.BreathIn.duration());
  Assets.Sounds.BreathIn.play(0, 1, 1, offset, grainDuration);
  let interval = Config.SOUND_INTERVAL / physics.particles.length + 100;
  setTimeout(player, interval);
}



function draw() {
  if (isMovingForward()) {
    moveForward();
    if (shouldMoveBackwards()) {
      changeDirection();
    }
  } else {
    moveBackwards();
    if (shouldMoveForward()) {
      changeDirection();
    }
  }
}

function setFontForTeam(team) {

  let font = Team2FontMap[team];
  if (!font) {
    font = 'Courier';
  }
  textFont(font);

}

function moveForward() {

  background(colorBackground);

  // Update the particles
  physics.update();

  //Draw the particles
  for (let particle of physics.particles) {
    setFontForTeam(particle.team);
    particle.draw();
  }

  if (mustAdvanceStrata()) {
    saveSnapshot();
    advanceStrata();
    changeBackground();

  }
}

function moveBackwards() {
  loadPixels();
  animation.currentSnapshot--;
  let current = snapshots[animation.currentSnapshot];
  if (!current) {
    debugger;
  }

  drawPixels(current);

  updatePixels();
}

function drawPixels(content) {
  for (let index = 0; index < pixels.length; index++) {
    pixels[index] = content[index];
  }
}

function mustAdvanceStrata() {
  return frameCount % Config.FRAMES_TO_EXPLODE == 0;
}

function advanceStrata() {

  let next = [];
  for (let item of physics.particles) {
    next = next.concat(item.explode());
  }

  physics.clear();
  physics.particles = next;
}

function changeBackground() {
  colorBackground = colorBackground - Config.BACKGRUND_COLOR_VARIATION;
}

function saveSnapshot() {
  loadPixels();
  let toSave = new Uint8ClampedArray(pixels.length);
  for (let index = 0; index < pixels.length; index++) {
    toSave[index] = pixels[index];
  }
  snapshots.push(toSave);
}