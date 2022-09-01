//update tick
import { STORE_COOKIES } from "./types";

const windowCookies = (cookies = {}) => ({
  type: STORE_COOKIES,
  payload: {
    cookie: { ...cookies },
  },
});

export default windowCookies;
