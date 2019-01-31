// Resources to be loaded from the assets folder

const Assets = {
    Fonts: {
        BebasNeue: null,
        GothamBook: null,
        Montserrat: null,
        RobotoCondensed: null
    },

    Sounds: {
        BreathIn: null,
        BreathOut: null
    }
};

function loadAssets() {
    Assets.Fonts.BebasNeue = loadFont('../assets/fonts/BebasNeue Bold.otf');
    Assets.Fonts.GothamBook = loadFont('../assets/fonts/Gotham-Book.ttf');
    Assets.Fonts.Montserrat = loadFont('../assets/fonts/Montserrat-Regular.otf');
    Assets.Fonts.RobotoCondensed = loadFont('../assets/fonts/RobotoCondensed-Light.ttf');

    Assets.Sounds.BreathIn = loadSound('../assets/sounds/breath_in.wav');
    Assets.Sounds.BreathOut = loadSound('../assets/sounds/breath_out.wav');

}