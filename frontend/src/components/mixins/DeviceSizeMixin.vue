
<script>
/**
 * Mixin to be included anywhere that screen size detection functionality
 */
export default {
  data() {
    return {
      screenWidth: undefined,
      screenHeight: undefined
    };
  },

  /**
   * Initializes the viewing browsers width and height as variables
   */
  created() {
    this.screenWidth = screen.width;
    this.screenHeight = screen.height;
  },
  methods: {
    /**
     * Checks to see whether the screen size is small
     * @return {boolean} true if screen is less than 1080px, false if not
     */
    isSmall() {
      if (this.screenWidth < 1080) {
        return true;
      } else {
        return false;
      }
    },

    /**
     * Checks to see whether the screen size is medium
     * @return {boolean} true if screen is greater than or equal to 1080px 
     * and less than 1536px, false if not
     */
    isMedium() {
      if (this.screenWidth >= 1080 && this.screenWidth < 1536) {
        return true;
      } else {
        return false;
      }
    },

    /**
     * Checks to see whether the screen size is small
     * @return {boolean} true if screen is greater than or equal to 
     * 1536px , false if not
     */
    isLarge() {
      if (this.screenWidth >= 1536) {
        return true;
      } else {
        return false;
      }
    }
  }
};
</script>
