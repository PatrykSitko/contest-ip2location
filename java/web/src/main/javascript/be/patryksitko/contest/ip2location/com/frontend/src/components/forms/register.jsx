import { Formik } from "formik";
import React from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { goBack } from "redux-first-routing";
import * as yup from "yup";
import "./register.scss";

const schema = yup.object({
  firstname: yup
    .string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Firstname is required."),
  lastname: yup
    .string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Lastname is required."),
  email: yup.string().email().required("Email is required."),
  password: yup.string().strongPassword().required("Password is required."),
  repassword: yup.string().required("Retyping password is required."),
});

const mapDispatchToProps = (dispatch) => ({
  goBack: (path) => dispatch(goBack(path)),
});

function LoginForm({ goBack }) {
  return (
    <div className="register-form-wrapper">
      <Formik
        validationSchema={schema}
        initialValues={{ firstname: "", lastname: "", email: "", password: "" }}
      >
        {({
          values,
          errors,
          touched,
          handleChange,
          handleBlur,
          handleSubmit,
          isSubmitting,
        }) => (
          <Form className="register-form" onSubmit={handleSubmit}>
            <Form.Group
              className="mb-3 given-names-wrapper firstname"
              controlId="formFirstname"
            >
              <Form.Label className="given-names">First name</Form.Label>
              <Form.Control
                name="firstname"
                className="given-names"
                type="text"
                placeholder="Enter first name"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.firstname}
                isValid={!errors.firstname && values.firstname !== ""}
                isInvalid={touched.firstname && errors.firstname}
                feedback={errors.firstname}
              />
              <Form.Control.Feedback type="invalid">
                {errors.firstname}
              </Form.Control.Feedback>
            </Form.Group>
            <Form.Group
              className="mb-3 given-names-wrapper lastname"
              controlId="formLastname"
            >
              <Form.Label className="given-names">Last name</Form.Label>
              <Form.Control
                name="lastname"
                className="given-names"
                type="text"
                placeholder="Enter last name"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.lastname}
                isValid={!errors.lastname && values.lastname !== ""}
                isInvalid={touched.lastname && errors.lastname}
                feedback={errors.lastname}
              />
              <Form.Control.Feedback type="invalid">
                {errors.lastname}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formEmail">
              <Form.Label className="email">Email address</Form.Label>
              <Form.Control
                name="email"
                type="email"
                placeholder="Enter email"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.email}
                isValid={!errors.email && values.email !== ""}
                isInvalid={touched.email && errors.email}
                feedback={errors.email}
              />
              <Form.Control.Feedback type="invalid">
                {errors.email}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group
              className="mb-3 form-group-password"
              controlId="formPassword"
            >
              <Button className="go-back" onClick={goBack}>
                go back
              </Button>
              <Form.Label className="password-label">Password</Form.Label>
              <Form.Control
                name="password"
                className="type-password"
                type="password"
                placeholder="Password"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.password}
                isValid={!errors.password && values.password !== ""}
                isInvalid={touched.password && errors.password}
                feedback={errors.password}
              />
            </Form.Group>
            <div className="feedback-invalid">{errors.password}</div>

            <Form.Group className="mb-3" controlId="formRetypePassword">
              <Form.Label className="password-label">
                Retype Password
              </Form.Label>
              <Form.Control type="password" placeholder="Retype Password" />
            </Form.Group>
            <Button variant="primary" type="submit" className="register">
              Register
            </Button>
          </Form>
        )}
      </Formik>
    </div>
  );
}

export default connect(null, mapDispatchToProps)(LoginForm);
