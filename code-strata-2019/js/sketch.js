
const Config = {
  FRAMES_TO_EXPLODE: 500, // Controls the number of frames a strata is visible
  PARTICLE_PADDING: 5, // Minimum distance between particles
  REPULSION_STRENGTH: -2000, // Strength of the repulsion between particles. Must be negative.
  JITTER: 0.0001, // Jitter for the repulsion
  MIN_SPEED: 2, // Minimum speed for a particle
  DRAG: 0.05, // Drag for particles

  INITIAL_BACKKGROUND_COLOR: 240,
  BACKGRUND_COLOR_VARIATION: 2,
};


let physics;
let data;
let colorBackground = Config.INITIAL_BACKKGROUND_COLOR;

// Map from teams to fonts
const Team2FontMap = {}


function getFontForTeam(team) {
  let font = Team2FontMap[team];
  if(!font) {
    font = Assets.Fonts.RobotoCondensed;
  }
  return font;
}

function preload() {

  loadAssets();
  
  data = new Data(
    loadJSON('../data/data.json'),
    loadJSON('../data/developers.json')
  );


}

function setup() {
  
  //Setting the team to font map
  Team2FontMap.javafx = Assets.Fonts.BebasNeue;
  Team2FontMap.App = Assets.Fonts.GothamBook;
  Team2FontMap.java = Assets.Fonts.Montserrat;

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
}

function draw() {
  background(colorBackground);
  physics.update();

  for (let particle of physics.particles) {
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