class Item {
  constructor(record, parent) {

    this.record = record;

    this.x = 0;
    this.y = 0;

    let boundingBox = font.textBounds(this.name, this.x, this.y);

    this.width = boundingBox.w;
    this.height = boundingBox.h;

    this.x = random(this.width / 2, windowWidth - this.width);
    this.y = random(this.height / 2, windowHeight - this.height / 2);

    this.parent = parent;
  }

  get name() {
    return this.record.name;
  }

  draw() {
    text(this.name, this.x, this.y);
  }

  createChildren() {
    return this.record.children.map(rec => new Item(rec, this));
  }

}