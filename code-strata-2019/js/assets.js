// Resources to be loaded from the assets folder

const Assets = {

    Sounds: {
        BreathIn: null,
        BreathOut: null
    }
};

function loadAssets() {
    Assets.Sounds.BreathIn = loadSound('../assets/sounds/breath_in.wav');
    Assets.Sounds.BreathOut = loadSound('../assets/sounds/breath_out.wav');
}