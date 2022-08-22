//update tick
import { UPDATE_TICK } from "./types";

export default (count) => ({
  type: UPDATE_TICK,
  payload: {
    updateTick: count,
  },
});
