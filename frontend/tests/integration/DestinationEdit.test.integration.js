import { shallowMount } from '@vue/test-utils';
import rules from "../../src/components/common/formRules";
import DestinationEdit from '../../src/components/destination/DestinationEdit';
import RollbackMixin from "travelea/src/components/mixins/RollbackMixin";

import Vuetify from "vuetify";
import Vue from "vue";

Vue.use(Vuetify);

describe('Test EditDestination.vue component', () => {
    it('ensures the RollbackMixin component is added as expected', () => {
        const $route = {
            params: {
                id: 1,
                dest_id: 1
            }
        };
        const wrapper = shallowMount(DestinationEdit, {

            mixins: [RollbackMixin],
            propsData: {
                typeList: [],
                destination: {},
                isError: false,
                ...rules
            },
            created: function() {
                this.typeList = ['test']
            },
            mocks: {
                $route
            }
        });

        const vm = wrapper.vm;

        // Jest does not run the lifecycle methods
        // It is your job to mock the mounted and created functions as shown above
        console.log('list', vm.typeList); // ~> ['test'], the created method is now called as part of the rendering lifecycle

        expect(vm.rollbackCanUndo()).toBe(false);
        expect(typeof vm.rollbackUndo).toBe('function');
    })
});

