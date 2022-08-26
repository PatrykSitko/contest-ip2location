import React from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { goBack } from "redux-first-routing";
import "./register.scss";

const mapDispatchToProps = (dispatch) => ({
  goBack: (path) => dispatch(goBack(path)),
});
function LoginForm({ goBack }) {
  return (
    <div className="register-form-wrapper">
      <Button className="go-back" onClick={goBack}>
        go back
      </Button>
      <Form className="register-form">
        <Form.Group
          className="mb-3 given-names-wrapper"
          controlId="formFirstname"
        >
          <Form.Label className="given-names">First name</Form.Label>
          <Form.Control
            className="given-names"
            type="text"
            placeholder="Enter first name"
          />
        </Form.Group>

        <Form.Group
          className="mb-3 given-names-wrapper"
          controlId="formLastname"
        >
          <Form.Label className="given-names">Last name</Form.Label>
          <Form.Control
            className="given-names"
            type="text"
            placeholder="Enter last name"
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formEmail">
          <Form.Label className="email">Email address</Form.Label>
          <Form.Control type="email" placeholder="Enter email" />
        </Form.Group>

        <Form.Group
          className="mb-3 form-group-password"
          controlId="formPassword"
        >
          <Form.Label className="password-label">Password</Form.Label>
          <Form.Control
            className="type-password"
            type="password"
            placeholder="Password"
          />
        </Form.Group>

        <Form.Group className="mb-3" controlId="formRetypePassword">
          <Form.Label className="password-label">Retype Password</Form.Label>
          <Form.Control type="password" placeholder="Retype Password" />
        </Form.Group>
        <Button variant="primary" type="submit" className="register">
          Register
        </Button>
      </Form>
    </div>
  );
}

export default connect(null, mapDispatchToProps)(LoginForm);
