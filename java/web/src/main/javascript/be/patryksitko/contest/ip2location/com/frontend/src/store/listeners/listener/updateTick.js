import updateTick from "../../actions/updateTick";

export default ({ getState, dispatch }) => {
  const {
    state: { updateTick: updateTickCount },
  } = getState();
  if (updateTickCount === 10) {
    dispatch(updateTick(0));
  } else {
    dispatch(updateTick(updateTickCount + 1));
  }
};
