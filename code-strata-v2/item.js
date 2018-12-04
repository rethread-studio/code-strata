const CLOSER = 100;
const FARTHER = 300;


class Item {
  constructor(record, x, y, parent) {
    this.record = record; // { "source": "...", "name": "...", children: [...] }

    this.x = x;
    this.y = y;

    this.parent = parent;

    this.developer = getDeveloper(record);
    // Get the size of the text

    let boundingBox = FONT.textBounds(this.name, this.x, this.y);

    this.width = boundingBox.w;
    this.height = boundingBox.h;

  }

  get name() {
    return this.developer;
  }

  draw() {
    text(this.name, this.x, this.y);
  }

  createChildren() {
    let angles = getRandomAngles(this.record.children.length);
    return this.record.children.map((rec, index) => {

      let module = 0;

      if(this.record.source == rec.source) {
        module = random() * CLOSER;
      }
      else {
        module = CLOSER + random() * (FARTHER - CLOSER);
      }
      return new Item(rec, this.x + module * Math.cos(angles[index]), this.y + module * Math.sin(angles[index]), this);
    });
  }

}

function getRandomAngles(count) {
  let angles = [];
  let previous = 0;
  for(let i=0; i <  count + 1; i++) {
    let value = previous + random();
    angles.push(value);
    previous = value;
  }
  let top = angles[angles.length - 1];
  for(let i=0; i < angles.length; i++) {
    angles[i] = 2 * Math.PI * angles[i] / top
  }
  return angles.slice(0, count);
}