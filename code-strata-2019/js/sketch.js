let physics;
let data;
let colorBackground = 240;

function preload() {
  Resources.FONT = loadFont('../assets/RobotoCondensed-Light.ttf');

  data = new Data(
    loadJSON('../data/data.json'),
    loadJSON('../data/developers.json')
  );
}

function setup() {
  createCanvas(windowWidth, windowHeight);
  textAlign(CENTER);

  physics = new VerletPhysics2D();
  physics.setDrag(0.05);
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

// function returnStrata() {

// }

function changeBackground() {
  colorBackground = colorBackground - 2;
}