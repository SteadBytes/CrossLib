# CrossLib

An example of implementing a shared core library to be wrapped in multiple
higher level libraries across multiple platforms e.g. Android, iOS, Python etc.

CrossLib provides facilities for randomly generating IDs. This was chosen to be
indicative of a self contained yet potentially complex set of functionality. In
this case the ID generation is little more than a wrapper around [UUID 4](https://en.wikipedia.org/wiki/Universally_unique_identifier)
however one can imagine more complex requirements e.g. application specific ID
schemes, security, performance etc.

