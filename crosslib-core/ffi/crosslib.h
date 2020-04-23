#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

typedef struct IDGenerator IDGenerator;

void id_generator_free(IDGenerator *ptr);

/**
 * Callers are responsible for freeing the returned pointer using
 * [`crosslib_str_free`]
 */
const unsigned char *id_generator_iterate(IDGenerator *ptr);

/**
 * Callers are responsible for freeing the returned pointer using
 * [`id_generator_free`]
 */
IDGenerator *id_generator_new(const unsigned char *seed, uint32_t max_iterations);
