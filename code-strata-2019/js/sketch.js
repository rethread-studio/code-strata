
const Config = {
  FRAMES_TO_EXPLODE: 500, // Controls the number of frames a strata is visible
  PARTICLE_PADDING: 5, // Minimum distance between particles
  REPULSION_STRENGTH: -2000, // Strength of the repulsion between particles. Must be negative.
  JITTER: 0.0001, // Jitter for the repulsion
  MIN_SPEED: 2, // Minimum speed for a particle
  DRAG: 0.05, // Drag for particles

  INITIAL_BACKKGROUND_COLOR: 240,
  BACKGRUND_COLOR_VARIATION: 2,

  MOVING_PARTICLE_COLOR: 0,
  LOCKED_PARTICLE_COLOR: 255,

  SOUND_INTERVAL = 4000,
  SOUND_FRAGMENT = 0.2,
};


let physics;
let data;
let colorBackground = Config.INITIAL_BACKKGROUND_COLOR;

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

  createCanvas(windowWidth, windowHeight);
  textAlign(CENTER);

  // Physical system
  physics = new VerletPhysics2D();
  physics.setDrag(Config.DRAG);
  physics.setWorldBounds(new Rect(0, 0, windowWidth, windowHeight));
  // Stratas will be represented as particles
  // The first strata is one particle with the root element
  physics.particles = [
    new Item(new Vec2D(windowWidth / 2, windowHeight / 2), data.root)
  ];

  setupPlayer();

}

function setupPlayer() {
  Assets.Sounds.BreathIn.playMode('sustain');
  player();

}

function player() {
  let grainDuration = random(Config.SOUND_INTERVAL, Assets.Sounds.BreathIn.duration());
  let offset = random(0, Config.SOUND_INTERVAL); // beginning of the sample
  Assets.Sounds.BreathIn.play(0, 1, 1, offset, grainDuration);

  let interval = Config.SOUND_INTERVAL / physics.particles.length;
  let playerTimeout = setTimeout(player, interval);
}

function draw() {
  background(colorBackground);
  physics.update();
  for(let particle of physics.particles) {
    textFont(Team2FontMap[particle.team]);
    particle.draw();
  }

  

  if (mustAdvance()) {
    advanceStrata();
    changeBackground();
    // interval = interval - 44;
  }
}

function mustAdvance() {
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