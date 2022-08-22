import FingerprintJS from "@fingerprintjs/fingerprintjs";
import { AUTHENTICATION__SET_FINGERPRINT } from "../../../actions/types";
export default async (store) => {
  const {
    state: { authentication },
  } = store.getState();
  const fp = await FingerprintJS.load();

  // The FingerprintJS agent is ready.
  // Get a visitor identifier when you'd like to.
  const result = await fp.get();

  // This is the visitor identifier:
  store.dispatch({
    type: AUTHENTICATION__SET_FINGERPRINT,
    payload: {
      authentication: {
        ...authentication,
        fingerprint: result.visitorId,
      },
    },
  });
};
