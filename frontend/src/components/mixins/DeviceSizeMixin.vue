
<script>
/**
 * Mixin to be included anywhere that screen size detection functionality
 */
export default {
  data() {
    return {
      windowsizes: {width: 0, height: 0}
    };
  },

  created() {
    this.initialize();
    window.addEventListener('resize', this.initialize);
  },

  methods: {
    /**
     * Initializes required variables within the page
     */
    initialize() {
      this.windowsizes.width = window.innerWidth;
      this.windowsizes.height = window.innerHeight;
    }
  },

  computed: {

    /**
     * Checks to see whether the screen size is small
     * @return {boolean} true if screen is less than 1080px, false if not
     */
    isSmall() {
      return this.windowsizes.width < 1080;
    },

    /**
     * Checks to see whether the screen size is medium
     * @return {boolean} true if screen is greater than or equal to 1080px 
     * and less than 1536px, false if not
     */
    isMedium() {
      return this.windowsizes.width >= 1080 && this.windowsizes.width < 1536;
    },

    /**
     * Checks to see whether the screen size is small
     * @return {boolean} true if screen is greater than or equal to 
     * 1536px , false if not
     */
    isLarge() {
      return this.windowsizes.width >= 1536;
    }
  }
};
</script>
