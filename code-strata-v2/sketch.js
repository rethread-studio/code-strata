// const CANVAS_WIDTH = 600;
// const CANVAS_HEIGHT = 400;

let FONT;
let DATA;
let DEVELOPERS;
//let stratas = [];


let STRATA;

let time_direction = 1;
let current_strata_index = 0;


function getDeveloper(record) {
  let choices = DEVELOPERS[record.source];
  if(!choices){
    return "";
  }
  return random(choices);
}


function preload() {
  FONT = loadFont('./assets/RobotoCondensed-Light.ttf');
  // font = loadFont('assets/LiberationSans-Regular.ttf');
  DATA = loadJSON('./data/data.json');
  DEVELOPERS = loadJSON('./data/developers.json')
}



function createStratas() {
  //stratas = [];

  let root = { source: "", name: "", children: [] };
  for(let thread of DATA.children) {
    root.children = root.children.concat(thread.children);
  }

  //Root item
  let item = new Item(root, windowWidth/2, windowHeight/2, null); // Parent will be null
  STRATA = [item];

  //stratas.push([item]); // The first strata will contain only the root item
  
  // for(let i=0; i < 10; i++) {
  // //while (true) {
  //   let lastStrata = stratas[stratas.length - 1];
  //   let children = [];
  //   for (let item of lastStrata) {
  //     children = children.concat(item.createChildren());
  //   }
  //   if (children.length > 0)
  //     stratas.push(children);
  //   else
  //     return;
  // }
}

function advanceStrata() {
  let next = [];
  for(let item of STRATA) {
    next = next.concat(item.createChildren());
  }
  STRATA = next;
}



function setup() {

  createCanvas(windowWidth, windowHeight);
  textAlign(CENTER);
  angleMode(DEGREES);
  frameRate(0.5);
  console.log("creating the stratas");
  createStratas();
}



function draw() {
  background(240);


  for(let item of STRATA) {
  //for (let item of stratas[current_strata_index]) {
    item.draw();
  }

  advanceStrata();

//   let next_strata = current_strata_index + time_direction;
//   if (next_strata < 0 || next_strata >= stratas.length)
//     time_direction = -time_direction;
//   current_strata_index += time_direction;
}