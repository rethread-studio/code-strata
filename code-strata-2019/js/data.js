class Data {

    constructor(records, developers) {
        this.records = records;
        this.developers = developers;
    }

    get root() {
        let root = { name: "ctrl-c ctrl-v", children: [] };
        for(let thread of this.records.children) {
          root.children = root.children.concat(thread.children);
        }
        return this.record(root);
    }

    getDeveloper(record) {
        let choices = this.developers[record.source];
        if(!choices) {
            return "";
        }
        return random(choices);
    }

    // Lazy (and random) developer assignment
    // and children retrieval
    record(values) {
        
        let result = { developer: this.getDeveloper(values) };
        Object.assign(result, values);
    
        result._children = result.children;
        result.children = undefined;

        let self = this;
        Object.defineProperty(result, "children", {
            get: function() {
                return this._children.map(rec => self.record(rec));
            }
        });

        return result;
    }
}