
<script>
/**
 * Mixin to be included anywhere that screen size detection functionality
 */
export default {
  data() {
    return {
      windowSizes: {width: 0, height: 0}
    };
  },

  created() {
    this.initialize();
    window.addEventListener("resize", this.initialize);
  },

  methods: {
    /**
     * Initializes required variables within the page
     */
    initialize() {
      this.windowSizes.width = window.innerWidth;
      this.windowSizes.height = window.innerHeight;
    }
  },

  computed: {
    /**
     * Checks to see whether the screen size is extra small
     * @return {boolean} true if screen is less than 600px, false if not
     */
    isExtraSmall() {
      return this.windowSizes.width < 600;
    },

    /**
     * Checks to see whether the screen size is small
     * @return {boolean} true if screen is greater than or equal to 600px 
     * and less than 1080px, false if nott
     */
    isSmall() {
      return this.windowSizes.width >= 600 && this.windowSizes.width < 960;
    },

    /**
     * Checks to see whether the screen size is medium
     * @return {boolean} true if screen is greater than or equal to 1080px 
     * and less than 1536px, false if not
     */
    isMedium() {
      return this.windowSizes.width >= 960 && this.windowSizes.width < 1264;
    },

    /**
     * Checks to see whether the screen size is large
     * @return {boolean} true if screen is greater than or equal to 
     * 1536px , false if not
     */
    isLarge() {
      return this.windowSizes.width >= 1264;
    },

    /**
     * Checks to see whether the screen size is extra large
     * @return {boolean} true if screen is greater than or equal to 
     * 1536px , false if not
     */
    isExtraLarge() {
      return this.windowSizes.width >= 1904;
    }
  }
};
</script>