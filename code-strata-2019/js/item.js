class Item extends VerletParticle2D {

  constructor(position, record, parent) {
    super(position);

    this.record = record;
    this.parent = parent;

    this.font = getFontForTeam(record.source); // External dependency :(

    let bbox = this.font.textBounds(this.text, this.x, this.y);

    this.width = bbox.w;
    this.height = bbox.h;
    this.radius = Math.max(this.width, this.height);

    this.addBehavior(new AttractionBehavior(this, this.radius + Config.PARTICLE_PADDING, Config.REPULSION_STRENGTH, Config.JITTER));
  }

  get text() {
    if(this.record['developer'] === undefined || this.record.developer === '') {
      return this.record.name;
    }
    return this.record.developer;
  }

  draw() {
    textFont(this.font);
    text(this.text, this.x, this.y);
  }

  explode() {
    return this.record.children.map(rec => {
      let item = new Item(new Vec2D(this.x, this.y), rec, this);
      let direction = new Vec2D.randomVector();
      let speed = Math.max(this.getVelocity().magnitude(), Config.MIN_SPEED);
      item.addVelocity(direction.scale(speed));
      return item;
    });
  }
}

