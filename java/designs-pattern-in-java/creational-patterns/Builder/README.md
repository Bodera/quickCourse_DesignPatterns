# README

> When construction gets a bit too complicated.

Suggested order to follow the lessons on this chapter:

- Builder Introduction
- Your First Builder
- Fluent Builder
- Faceted Builder

## Summary

- A builder is a separate component whose only purpose in life is for building up one particular object.
- You can either give the builder a constructor there by letting it exist as a separate stand alone component that somebody instantiates and then uses to build something or you can return via a static function or indeed a static _getter_ from the component that you're actually trying to build for example.
- To make a builder fluent you simply return _this_.
- Different facets of an object can be built with different builders working in tandem via a base class.
