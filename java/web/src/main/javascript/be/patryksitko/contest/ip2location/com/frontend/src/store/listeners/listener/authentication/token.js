import { AUTHENTICATION__SET_TOKEN } from "../../../actions/types";
export default (store) => {
  const {
    state: { authentication },
  } = store.getState();
  if (
    authentication.token !== window.localStorage.getItem("authentication-token")
  ) {
    store.dispatch({
      type: AUTHENTICATION__SET_TOKEN,
      payload: {
        authentication: {
          ...authentication,
          token: window.localStorage.getItem("authentication-token"),
        },
      },
    });
  }
};
