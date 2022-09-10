import React from "react";
import { connect } from "react-redux";
import useCheckAuthenticationToken from "../../hooks/useCheckAuthenticationToken";

const mapStateToProps = ({ state }) => ({
  csrfToken: state.cookie.csrfToken,
  fingerprint: state.fingerprint,
  authenticationToken: state.cookie["authentication-token"],
});

function LandingPage({ csrfToken, fingerprint, authenticationToken }) {
  useCheckAuthenticationToken(csrfToken, fingerprint, authenticationToken);
  return <div>Landing page</div>;
}

export default connect(mapStateToProps)(LandingPage);
