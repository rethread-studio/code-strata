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

  get team() {
    return this.record['source'];
  }

  draw() {
    //textFont(this.font); // It turns out that setting the font makes the process really slow.
    //fill(color(this.isLocked?Config.LOCKED_PARTICLE_COLOR:Config.MOVING_PARTICLE_COLOR));
    text(this.text, this.x, this.y);

    // fill(color(0))
    // ellipse(this.x, this.y, this.radius + Config.PARTICLE_PADDING, this.radius + Config.PARTICLE_PADDING);
    // fill(color(255));
    // ellipse(this.x, this.y, this.radius, this.radius);
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

