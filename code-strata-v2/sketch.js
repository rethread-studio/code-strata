// const CANVAS_WIDTH = 600;
// const CANVAS_HEIGHT = 400;

let font;
let data;
let stratas = [];
let time_direction = 1;
let current_strata_index = 0;



function preload() {
  font = loadFont('./assets/RobotoCondensed-Light.ttf');
  // font = loadFont('assets/LiberationSans-Regular.ttf');
  data = loadJSON('./data/tree.json')

}



function createStratas() {
  stratas = [];

  //Root item
  // WARNING: I'm showing the name of the function instead of the
  // developer's name
  let item = new Item(data); // Parent will be null

  //Place the item in the middle of the screen
  item.x = random(item.width / 2, windowWidth - item.width);
  item.y = random(item.height / 2, windowHeight - item.height / 2);

  stratas.push([item]); // The first strata will contain only the root item

  while (true) {
    let lastStrata = stratas[stratas.length - 1];
    let children = [];
    for (let item of lastStrata) {
      children = children.concat(item.createChildren());
    }
    if (children.length > 0)
      stratas.push(children);
    else
      return;
  }
}



function setup() {

  createCanvas(windowWidth, windowHeight);
  textAlign(CENTER);
  angleMode(DEGREES);
  frameRate(0.5);

  createStratas();
}



function draw() {
  background(240);

  for (let item of stratas[current_strata_index]) {
    item.draw();
  }

  let next_strata = current_strata_index + time_direction;
  if (next_strata < 0 || next_strata >= stratas.length)
    time_direction = -time_direction;
  current_strata_index += time_direction;
}