use rand::{rngs::StdRng, Rng, SeedableRng};
use uuid::{Builder, Uuid, Variant, Version};

#[derive(Debug)]
pub struct IDGenerator {
    rng: StdRng,
    remaining_iterations: u32,
}

impl IDGenerator {
    pub fn new(seed: [u8; 32], max_iterations: u32) -> IDGenerator {
        IDGenerator {
            rng: StdRng::from_seed(seed),
            remaining_iterations: max_iterations,
        }
    }
}

impl Iterator for IDGenerator {
    type Item = Uuid;

    fn next(&mut self) -> Option<Self::Item> {
        if self.remaining_iterations == 0 {
            return None;
        }

        let mut rand_bytes = [0; 16];
        self.rng.fill(&mut rand_bytes);
        self.remaining_iterations -= 1;

        let uuid = Builder::from_bytes(rand_bytes)
            .set_variant(Variant::RFC4122)
            .set_version(Version::Random)
            .build();

        Some(uuid)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn max_iterations() {
        let seed = [
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xa, 0xb, 0xc, 0xd, 0xe, 0xf, 0x10,
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e,
            0x1f,
        ];
        let max_iterations = 2048;

        let gen = IDGenerator::new(seed, max_iterations);

        let vals: Vec<Uuid> = gen.into_iter().collect();

        assert_eq!(vals.len(), max_iterations as usize);
    }
}
