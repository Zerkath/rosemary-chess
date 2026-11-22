-- Self (exclusive) CPU samples per method, attributed to the application code
-- that invoked it. The hottest leaf frames are usually low-level JDK/native
-- helpers (itable stubs, arraycopy, ...), so each sample is also charged to the
-- closest rosemary/* frame further up the stack: the caller actually responsible
-- for that work. Samples with no application frame (JVM/GC threads) are skipped.
-- Run with:
--   trace_processor query /tmp/rosemary-profile.pb.gz -f scripts/hottestMethods.sql
WITH totals AS (SELECT sum(value) AS total FROM __intrinsic_aggregate_sample),
attributed AS (
    SELECT
        s.value,
        (SELECT f.name
         FROM __intrinsic_stack_profile_callsite c
         JOIN __intrinsic_stack_profile_frame f ON f.id = c.frame_id
         WHERE c.id = s.callsite_id) AS method,
        (SELECT f.name
         FROM experimental_ancestor_stack_profile_callsite(s.callsite_id) a
         JOIN __intrinsic_stack_profile_frame f ON f.id = a.frame_id
         WHERE f.name LIKE 'rosemary/%'
         ORDER BY a.depth DESC
         LIMIT 1) AS caller
    FROM __intrinsic_aggregate_sample s
)
SELECT
    method,
    caller,
    CAST(sum(value) AS INTEGER) AS samples,
    round(100.0 * sum(value) / (SELECT total FROM totals), 2) AS pct
FROM attributed
WHERE caller IS NOT NULL
GROUP BY method, caller
ORDER BY samples DESC
LIMIT 40;
